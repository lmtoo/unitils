/*
 * Copyright 2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unitils.dbmaintainer.structure.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.unitils.core.dbsupport.DbSupport;
import org.unitils.dbmaintainer.structure.ConstraintsDisabler;
import org.unitils.dbmaintainer.util.BaseDatabaseTask;

import java.sql.Connection;
import java.util.Properties;
import java.util.Set;

/**
 * Implementation of {@link ConstraintsDisabler} for a DBMS with following properties:
 * <ul>
 * <li>Constraints can be disabled permanently and individually</li>
 * <li>Foreign key constraints checking cannot be disabled on a JDBC connection<li>
 * </ul>
 * Examples of such a DBMS are Oracle and DB2.
 *
 * @author Filip Neven
 * @author Bart Vermeiren
 * @author Tim Ducheyne
 */
public class OracleStyleConstraintsDisabler extends BaseDatabaseTask implements ConstraintsDisabler {

    /* The logger instance for this class */
    private static Log logger = LogFactory.getLog(OracleStyleConstraintsDisabler.class);


    /**
     * Initializes the disabler.
     *
     * @param configuration the config, not null
     */
    protected void doInit(Properties configuration) {
    }


    /**
     * Permanently disable every foreign key or not-null constraint
     */
    public void disableConstraints() {
        for (DbSupport dbSupport : dbSupports) {
            logger.info("Disabling contraints in database schema " + dbSupport.getSchemaName());
            removeNotNullConstraints(dbSupport);
        }
    }


    /**
     * Not supported for oracle style.
     *
     * @param connection The db connection to use, not null
     */
    public void disableConstraintsOnConnection(Connection connection) {
    }


    /**
     * Sends statements to the StatementHandler that make sure all not-null constraints are disabled.
     *
     * @param dbSupport The database support, not null
     */
    protected void removeNotNullConstraints(DbSupport dbSupport) {
        Set<String> tableNames = dbSupport.getTableNames();
        for (String tableName : tableNames) {
            Set<String> constraintNames = dbSupport.getTableConstraintNames(tableName);
            for (String constraintName : constraintNames) {
                dbSupport.disableConstraint(tableName, constraintName);
            }
        }
    }

}