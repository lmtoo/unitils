/*
 * Copyright Unitils.org
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
package org.unitils.dataset.loadstrategy.loader.impl;

import org.unitils.core.UnitilsException;
import org.unitils.dataset.database.DatabaseAccessor;
import org.unitils.dataset.loadstrategy.impl.DataSetRowProcessor;
import org.unitils.dataset.loadstrategy.loader.DataSetLoader;
import org.unitils.dataset.model.database.Row;
import org.unitils.dataset.model.dataset.DataSetRow;
import org.unitils.dataset.rowsource.DataSetRowSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base class for loading data sets.
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public abstract class BaseDataSetLoader implements DataSetLoader {

    protected DataSetRowProcessor dataSetRowProcessor;
    protected DatabaseAccessor databaseAccessor;


    /**
     * @param dataSetRowProcessor Processes data set rows so that they are ready to be loaded in the database, not null
     * @param databaseAccessor    The accessor for the database, not null
     */
    public void init(DataSetRowProcessor dataSetRowProcessor, DatabaseAccessor databaseAccessor) {
        this.dataSetRowProcessor = dataSetRowProcessor;
        this.databaseAccessor = databaseAccessor;
    }


    /**
     * Loads the rows provided by the given data set row source.
     *
     * @param dataSetRowSource The source that will provide the data set rows, not null
     * @param variables        The variable values that will be filled into the data set rows, not null
     */
    public void load(DataSetRowSource dataSetRowSource, List<String> variables) {
        DataSetRow dataSetRow;
        while ((dataSetRow = dataSetRowSource.getNextDataSetRow()) != null) {
            loadDataSetRow(dataSetRow, variables);
        }
    }


    protected int loadDataSetRow(DataSetRow dataSetRow, List<String> variables) {
        try {
            Row row = processDataSetRow(dataSetRow, variables);
            if (row.isEmpty()) {
                return 0;
            }
            return loadRow(row);

        } catch (Exception e) {
            throw new UnitilsException("Unable to load data set row: " + dataSetRow + ", variables: " + variables, e);
        }
    }

    protected Row processDataSetRow(DataSetRow dataSetRow, List<String> variables) throws Exception {
        Set<String> unusedPrimaryKeyColumnNames = new HashSet<String>();
        Row row = dataSetRowProcessor.process(dataSetRow, variables, unusedPrimaryKeyColumnNames);
        if (!unusedPrimaryKeyColumnNames.isEmpty()) {
            handleUnusedPrimaryKeyColumns(unusedPrimaryKeyColumnNames);
        }
        return row;
    }


    /**
     * Hook method that is called when there was not a value for every of the PK column.
     *
     * @param unusedPrimaryKeyColumnNames The names of the PK columns that did not have a value, not null
     */
    protected void handleUnusedPrimaryKeyColumns(Set<String> unusedPrimaryKeyColumnNames) {
        // nothing to do
    }

    /**
     * Performs the actual loading operation.
     *
     * @param row The row to load, not null
     * @return The update count
     */
    protected abstract int loadRow(Row row) throws Exception;
}