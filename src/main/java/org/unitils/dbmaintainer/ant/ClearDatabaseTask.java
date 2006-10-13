package org.unitils.dbmaintainer.ant;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.unitils.core.Unitils;
import org.unitils.dbmaintainer.clear.DBClearer;
import org.unitils.dbmaintainer.handler.JDBCStatementHandler;
import org.unitils.dbmaintainer.handler.StatementHandler;
import org.unitils.dbmaintainer.handler.StatementHandlerException;
import org.unitils.util.ReflectionUtils;

/**
 * Ant tasks that drops all database tables in the current database
 */
public class ClearDatabaseTask extends BaseUnitilsTask {

    private static final Logger logger = Logger.getLogger(ClearDatabaseTask.class);

    /* Property key of the implementation class of the {@link DBClearer} */
    public static final String PROPKEY_DBCLEARER_START = "dbMaintainer.dbClearer.className";

    /* Property key of the SQL dialect of the underlying DBMS implementation */
    private static final String PROPKEY_DATABASE_DIALECT = "database.dialect";

    public void doExecute() throws BuildException {
        try {
            DBClearer dbClearer = createDBClearer();
            dbClearer.clearDatabase();
        } catch (StatementHandlerException e) {
            logger.error(e);
            throw new BuildException("Error while clearing database", e);
        }
    }

    private DBClearer createDBClearer() {
        Configuration configuration = Unitils.getInstance().getConfiguration();

        StatementHandler statementHandler = new JDBCStatementHandler();
        statementHandler.init(configuration, dataSource);

        DBClearer dbClearer = ReflectionUtils.createInstanceOfType(configuration.getString(PROPKEY_DBCLEARER_START + configuration.getString(PROPKEY_DATABASE_DIALECT)));
        dbClearer.init(configuration, dataSource, statementHandler);
        return dbClearer;
    }
}
