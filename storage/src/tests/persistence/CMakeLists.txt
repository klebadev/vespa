# Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.

vespa_add_executable(storage_persistence_gtest_runner_app TEST
    SOURCES
    bucketownershipnotifiertest.cpp
    has_mask_remapper_test.cpp
    mergehandlertest.cpp
    persistencequeuetest.cpp
    persistencetestutils.cpp
    persistencethread_splittest.cpp
    processalltest.cpp
    provider_error_wrapper_test.cpp
    splitbitdetectortest.cpp
    testandsettest.cpp
    gtest_runner.cpp
    DEPENDS
    storage
    storage_testpersistence_common
    GTest::GTest
)

vespa_add_test(
    NAME storage_persistence_gtest_runner_app
    COMMAND storage_persistence_gtest_runner_app
)
