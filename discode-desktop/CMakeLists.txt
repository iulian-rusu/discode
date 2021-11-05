cmake_minimum_required(VERSION 3.21)

project(discode-desktop VERSION 0.1 LANGUAGES CXX)

if(WIN32)
    list(APPEND CMAKE_MODULE_PATH "${CMAKE_CURRENT_SOURCE_DIR}/cmake")
    include(deploy_qt)
endif()

set(CMAKE_INCLUDE_CURRENT_DIR ON)

set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
set(CMAKE_AUTOUIC ON)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(QT NAMES Qt6 COMPONENTS Core Quick REQUIRED)
find_package(Qt${QT_VERSION_MAJOR} COMPONENTS Core Quick REQUIRED)

add_subdirectory(controller)
add_subdirectory(model)
add_subdirectory(network)
add_subdirectory(view)

set(PROJECT_SOURCES
    main.cpp
    qml.qrc
)

qt_add_executable(discode-desktop
    MANUAL_FINALIZATION
    ${PROJECT_SOURCES}
)

if(WIN32)
    deploy_qt(${PROJECT_NAME})
endif()

target_compile_definitions(discode-desktop
  PRIVATE $<$<OR:$<CONFIG:Debug>,$<CONFIG:RelWithDebInfo>>:QT_QML_DEBUG>)
target_link_libraries(discode-desktop
  PRIVATE Qt${QT_VERSION_MAJOR}::Core Qt${QT_VERSION_MAJOR}::Quick)

qt_import_qml_plugins(discode-desktop)
qt_finalize_executable(discode-desktop)
