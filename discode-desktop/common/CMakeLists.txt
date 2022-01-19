add_library(common STATIC
    ban.h
    report.h
    session.h
    ban_model_roles.h
        service_ips.h service_routes.h)

target_include_directories(common PUBLIC .)
target_link_libraries(common PUBLIC Qt${QT_VERSION_MAJOR}::Core)
