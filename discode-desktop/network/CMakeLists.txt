add_library(network STATIC
    authentication_service.cpp
    authentication_service.h
    ban_service.cpp
    ban_service.h
    network_helpers.h
    report_service.cpp
    report_service.h
    api_config.h
)

target_include_directories(network PUBLIC .)
target_link_libraries(network PUBLIC Qt${QT_VERSION_MAJOR}::Core Qt${QT_VERSION_MAJOR}::Network common)
