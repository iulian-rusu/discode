add_library(model STATIC
    ban_model.cpp
    ban_model.h
    report_model.cpp
    report_model.h
)

target_include_directories(model PUBLIC .)
target_link_libraries(model PUBLIC Qt${QT_VERSION_MAJOR}::Core common)
