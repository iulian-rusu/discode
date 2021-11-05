include_guard(GLOBAL)
function(deploy_qt target)
    if (WIN32)
        get_target_property(_qmake_executable Qt::qmake IMPORTED_LOCATION)
        get_filename_component(_qt_bin_dir "${_qmake_executable}" DIRECTORY)
        find_program(WINDEPLOYQT_EXECUTABLE windeployqt HINTS "${_qt_bin_dir}")
        add_custom_command(TARGET ${target} POST_BUILD
            COMMAND "${CMAKE_COMMAND}" -E
                env PATH="${_qt_bin_dir}" "${WINDEPLOYQT_EXECUTABLE}" --qmldir "${CMAKE_CURRENT_SOURCE_DIR}/view"
                "$<TARGET_FILE:${target}>"
            COMMENT "Running windeployqt..."
            )
    endif()
endfunction()
