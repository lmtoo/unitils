/*
 * Copyright (C) 2006, Ordina
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.unitils.core;

import org.apache.commons.configuration.Configuration;

import java.lang.reflect.Method;
import java.util.List;

/**
 * todo javadoc
 * <p/>
 * todo implement module
 * todo remove test context
 */
public class Unitils {

    private static Unitils unitils = new Unitils();

    public static Unitils getInstance() {
        return unitils;
    }

    private TestListener testListener;

    //todo javadoc
    private ModulesRepository modulesRepository;

    //todo javadoc
    private Configuration configuration;

    private TestContext testContext;


    // Loading core will be done the first time only
    //todo
    public Unitils() {

        testContext = new TestContext();
        testListener = new UnitilsTestListener();
        configuration = createConfiguration();
        modulesRepository = createModulesRepository(configuration);
    }


    public TestListener getTestListener() {
        return testListener;
    }


    public ModulesRepository getModulesRepository() {
        return modulesRepository;
    }


    public TestContext getTestContext() {
        return testContext;
    }


    public Configuration getConfiguration() {
        return configuration;
    }


    protected ModulesRepository createModulesRepository(Configuration configuration) {

        ModulesLoader modulesLoader = new ModulesLoader();
        List<Module> modules = modulesLoader.loadModules(configuration);
        return new ModulesRepository(modules);
    }

    protected Configuration createConfiguration() {

        ConfigurationLoader configurationLoader = new ConfigurationLoader();
        return configurationLoader.loadConfiguration();
    }


    private class UnitilsTestListener extends TestListener {


        @Override
        public void beforeAll() {

            TestContext testContext = getTestContext();
            testContext.setTestClass(null);
            testContext.setTestObject(null);
            testContext.setTestMethod(null);

            List<Module> modules = modulesRepository.getModules();
            for (Module module : modules) {
                modulesRepository.getTestListener(module).beforeAll();
            }
        }


        @Override
        public void beforeTestClass(Class testClass) {

            TestContext testContext = getTestContext();
            testContext.setTestClass(testClass);
            testContext.setTestObject(null);
            testContext.setTestMethod(null);

            List<Module> modules = modulesRepository.getModules();
            for (Module module : modules) {
                modulesRepository.getTestListener(module).beforeTestClass(testClass);
            }
        }


        @Override
        public void beforeTestSetUp(Object testObject) {

            TestContext testContext = getTestContext();
            testContext.setTestClass(testObject.getClass());
            testContext.setTestObject(testObject);
            testContext.setTestMethod(null);

            List<Module> modules = modulesRepository.getModules();
            for (Module module : modules) {
                modulesRepository.getTestListener(module).beforeTestSetUp(testObject);
            }
        }


        @Override
        public void beforeTestMethod(Object testObject, Method testMethod) {

            TestContext testContext = getTestContext();
            testContext.setTestClass(testObject.getClass());
            testContext.setTestObject(testObject);
            testContext.setTestMethod(testMethod);

            List<Module> modules = modulesRepository.getModules();
            for (Module module : modules) {
                modulesRepository.getTestListener(module).beforeTestMethod(testObject, testMethod);
            }
        }


        @Override
        public void afterTestMethod(Object testObject, Method testMethod) {

            TestContext testContext = getTestContext();
            testContext.setTestClass(testObject.getClass());
            testContext.setTestObject(testObject);
            testContext.setTestMethod(testMethod);

            List<Module> modules = modulesRepository.getModules();
            for (Module module : modules) {
                modulesRepository.getTestListener(module).afterTestMethod(testObject, testMethod);
            }
        }


        @Override
        public void afterTestTearDown(Object testObject) {

            TestContext testContext = getTestContext();
            testContext.setTestClass(testObject.getClass());
            testContext.setTestObject(testObject);
            testContext.setTestMethod(null);

            List<Module> modules = modulesRepository.getModules();
            for (Module module : modules) {
                modulesRepository.getTestListener(module).afterTestTearDown(testObject);
            }
        }


        @Override
        public void afterTestClass(Class testClass) {

            TestContext testContext = getTestContext();
            testContext.setTestClass(testClass);
            testContext.setTestObject(null);
            testContext.setTestMethod(null);

            List<Module> modules = modulesRepository.getModules();
            for (Module module : modules) {
                modulesRepository.getTestListener(module).afterTestClass(testClass);
            }
        }


        @Override
        public void afterAll() {

            TestContext testContext = getTestContext();
            testContext.setTestClass(null);
            testContext.setTestObject(null);
            testContext.setTestMethod(null);

            List<Module> modules = modulesRepository.getModules();
            for (Module module : modules) {
                modulesRepository.getTestListener(module).afterAll();
            }
        }
    }

}