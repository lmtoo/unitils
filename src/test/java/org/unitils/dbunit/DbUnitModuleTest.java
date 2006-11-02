package org.unitils.dbunit;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import static org.easymock.EasyMock.expect;
import org.unitils.UnitilsJUnit3;
import org.unitils.core.ModulesRepository;
import org.unitils.db.DatabaseModule;
import org.unitils.db.annotations.DatabaseTest;
import org.unitils.easymock.annotation.LenientMock;

/**
 * Test class for the DbUnitModule
 */
@SuppressWarnings({"UnusedDeclaration"})
public class DbUnitModuleTest extends UnitilsJUnit3 {

    /**
     * Temp dir where test script files are put during the tests
     */
    private static final String DBCHANGE_FILE_DIRECTORY = System.getProperty("java.io.tmpdir") + "/FileScriptSourceTest/";

    /**
     * First test script file
     */
    private static final String DBCHANGE_FILE1 = "001_script.sql";

    /**
     * Second test script file
     */
    private static final String DBCHANGE_FILE2 = "002_script.sql";

    /**
     * Path of first test script file on the file system
     */
    private static final String DBCHANGE_FILE1_FILESYSTEM = DBCHANGE_FILE_DIRECTORY + DBCHANGE_FILE1;

    /**
     * Path of second test script file on the file system
     */
    private static final String DBCHANGE_FILE2_FILESYSTEM = DBCHANGE_FILE_DIRECTORY + DBCHANGE_FILE2;

    /**
     * Tested object
     */
    private DbUnitModule dbUnitModule;

    @LenientMock
    private IDatabaseConnection mockDbUnitDatabaseConnection;

    @LenientMock
    private ModulesRepository mockModulesRepository;

    @LenientMock
    private DatabaseModule mockDatabaseModule;

    @LenientMock
    private DatabaseOperation mockInsertDatabaseOperation;

    @LenientMock
    private IDataSet mockDbUnitDataSet;

    /**
     * Configures the tested instance. Makes sure it uses the correct mock objects.
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();

        dbUnitModule = new DbUnitModule() {
            protected IDatabaseConnection createDbUnitConnection() {
                return mockDbUnitDatabaseConnection;
            }

            protected DatabaseOperation getInsertDatabaseOperation() {
                return mockInsertDatabaseOperation;
            }
        };

        expect(mockModulesRepository.getFirstModule(DatabaseModule.class)).andStubReturn(mockDatabaseModule);
    }

    /**
     * Tests wether the test object is correctly regarded as a database test
     * @throws Exception
     */
    public void testIsDatabaseTest() throws Exception {

        assertTrue(dbUnitModule.isDatabaseTest(DbTest.class));
    }

    /**
     * Tests that the DbUnitDatabaseConnection is correctly returned
     */
    public void testInitDbUnitConnection() {
        dbUnitModule.initDbUnitConnection();
        assertSame(mockDbUnitDatabaseConnection, dbUnitModule.getDbUnitDatabaseConnection());
    }


    //Todo implement
    public void testInsertTestData_noDbUnitDataSetAnnotation() throws Exception {

        dbUnitModule.insertTestData(DbTest.class, DbTest.class.getDeclaredMethod("testMethod"));

    }


    @DatabaseTest
    public static class DbTest {

        public void testMethod() {
        }
    }

}