/*
 * Copyright (C) 2006, Ordina
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.unitils.dbmaintainer.maintainer;

import static org.easymock.classextension.EasyMock.expect;
import static org.easymock.classextension.EasyMock.expectLastCall;
import org.unitils.UnitilsJUnit3;
import org.unitils.dbmaintainer.clear.DBClearer;
import org.unitils.dbmaintainer.constraints.ConstraintsDisabler;
import org.unitils.dbmaintainer.dtd.DtdGenerator;
import org.unitils.dbmaintainer.handler.StatementHandlerException;
import org.unitils.dbmaintainer.maintainer.script.ScriptSource;
import org.unitils.dbmaintainer.maintainer.version.Version;
import org.unitils.dbmaintainer.maintainer.version.VersionSource;
import org.unitils.dbmaintainer.script.SQLScriptRunner;
import org.unitils.dbmaintainer.sequences.SequenceUpdater;
import static org.unitils.easymock.EasyMockUnitils.replay;
import org.unitils.easymock.annotation.LenientMock;
import org.unitils.inject.annotation.AutoInject;
import org.unitils.inject.annotation.TestedObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the main algorithm of the DBMaintainer, using mocks for all implementation classes.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class DBMaintainerTest extends UnitilsJUnit3 {

    @LenientMock
    @AutoInject
    private VersionSource mockVersionSource;

    @LenientMock
    @AutoInject
    private ScriptSource mockScriptSource;

    @LenientMock
    @AutoInject
    private SQLScriptRunner mockScriptRunner;

    @LenientMock
    @AutoInject
    private DBClearer mockDbClearer;

    @LenientMock
    @AutoInject
    private ConstraintsDisabler mockConstraintsDisabler;

    @LenientMock
    @AutoInject
    private SequenceUpdater mockSequenceUpdater;

    @LenientMock
    @AutoInject
    private DtdGenerator mockDtdGenerator;

    @TestedObject
    private DBMaintainer dbMaintainer;

    /* Test database update scripts */
    private List<VersionScriptPair> versionScriptPairs;

    /* Test database versions */
    private Version version0, version1, version2;


    /**
     * Create an instance of DBMaintainer
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();

        dbMaintainer = new DBMaintainer();
        dbMaintainer.setFromScratchEnabled(true);

        versionScriptPairs = new ArrayList<VersionScriptPair>();
        version0 = new Version(0L, 0L);
        version1 = new Version(1L, 1L);
        version2 = new Version(2L, 2L);
        versionScriptPairs.add(new VersionScriptPair(version1, "Script 1"));
        versionScriptPairs.add(new VersionScriptPair(version2, "Script 2"));
    }

    /**
     * Tests incremental update of a database: No existing scripts are modified, but new ones are added. The database
     * is not cleared but the new scripts are executed on by one, incrementing the database version each time.
     */
    public void testDBMaintainer_incremental() throws Exception {
        // Record behavior
        expect(mockVersionSource.getDbVersion()).andReturn(version0);
        expect(mockScriptSource.existingScriptsModified(version0)).andReturn(false);
        expect(mockScriptSource.getNewScripts(version0)).andReturn(versionScriptPairs);
        mockScriptRunner.execute("Script 1");
        mockVersionSource.setDbVersion(version1);
        mockScriptRunner.execute("Script 2");
        mockVersionSource.setDbVersion(version2);
        mockConstraintsDisabler.disableConstraints();
        mockSequenceUpdater.updateSequences();
        mockDtdGenerator.generateDtd();
        replay();

        // Execute test
        dbMaintainer.updateDatabase();
    }

    /**
     * Tests updating the database from scratch: Existing scripts have been modified. The database is cleared first
     * and all scripts are executed.
     */
    public void testDBMaintainer_fromScratch() throws Exception {
        // Record behavior
        expect(mockVersionSource.getDbVersion()).andReturn(version0);
        expect(mockScriptSource.existingScriptsModified(version0)).andReturn(true);
        mockDbClearer.clearDatabase();
        expect(mockScriptSource.getAllScripts()).andReturn(versionScriptPairs);
        mockScriptRunner.execute("Script 1");
        mockVersionSource.setDbVersion(version1);
        mockScriptRunner.execute("Script 2");
        mockVersionSource.setDbVersion(version2);
        mockConstraintsDisabler.disableConstraints();
        mockSequenceUpdater.updateSequences();
        mockDtdGenerator.generateDtd();
        replay();

        // Execute test
        dbMaintainer.updateDatabase();
    }

    /**
     * Tests the behavior in case there is an error in a script supplied by the ScriptSource. In this case, the
     * database version must not org incremented and a StatementHandlerException must be thrown.
     */
    public void testDBMaintainer_errorInScript() throws Exception {
        expect(mockVersionSource.getDbVersion()).andReturn(version0).anyTimes();
        expect(mockScriptSource.existingScriptsModified(version0)).andReturn(false);
        expect(mockScriptSource.getNewScripts(version0)).andReturn(versionScriptPairs);
        mockScriptRunner.execute("Script 1");
        expectLastCall().andThrow(new StatementHandlerException("Test exception"));

        replay();

        try {
            dbMaintainer.updateDatabase();
            fail("A StatementHandlerException should have been thrown");
        } catch (StatementHandlerException e) {
            // Expected
        }
    }

}