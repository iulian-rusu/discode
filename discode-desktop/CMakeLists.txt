cmake_minimum_required(VERSION 3.20)

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

if (MSVC)
    add_compile_options(/W4)
    if (CMAKE_BUILD_TYPE MATCHES Debug)
        add_compile_options(/O2)
    else()
        add_compile_options(/Od)
    endif()
else()
    add_compile_options(-Wall -Wextra -Wpedantic -Wuninitialized -Wshadow -Wunused)
    if (CMAKE_BUILD_TYPE MATCHES Debug)
        add_compile_options(-Og)
    else()
        add_compile_options(-O3)
    endif()
endif()

set(QT_FEATURE_intelcet ON)
find_package(QT NAMES Qt6 COMPONENTS Core Quick Network REQUIRED)
find_package(Qt${QT_VERSION_MAJOR} COMPONENTS Core Quick Network REQUIRED)

add_subdirectory(controller)
add_subdirectory(model)
add_subdirectory(network)
add_subdirectory(view)

set(PROJECT_SOURCES
    main.cpp
    qml.qrc
)

qt_add_executable(${PROJECT_NAME}
    MANUAL_FINALIZATION
    ${PROJECT_SOURCES}
)

if(WIN32)
    deploy_qt(${PROJECT_NAME})
endif()

target_compile_definitions(${PROJECT_NAME}
  PRIVATE $<$<OR:$<CONFIG:Debug>,$<CONFIG:RelWithDebInfo>>:QT_QML_DEBUG>)
target_link_libraries(${PROJECT_NAME}
  PRIVATE Qt${QT_VERSION_MAJOR}::Core Qt${QT_VERSION_MAJOR}::Quick controller model network view)

target_compile_definitions(${PROJECT_NAME} PUBLIC APP_ICON_PATH="${CMAKE_CURRENT_SOURCE_DIR}/view/resources/icons/favicon.png")
target_compile_definitions(${PROJECT_NAME} PUBLIC COLORS_PATH="${CMAKE_CURRENT_SOURCE_DIR}/view/resources/Colors.qml")
target_compile_definitions(${PROJECT_NAME} PUBLIC FONT_PATH="${CMAKE_CURRENT_SOURCE_DIR}/view/resources/Font.qml")

qt_import_qml_plugins(${PROJECT_NAME})
qt_finalize_executable(${PROJECT_NAME})
