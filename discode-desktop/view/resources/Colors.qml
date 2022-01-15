pragma Singleton

import QtQuick

Item {
    readonly property color background: "#2C2F33"
    readonly property color foreground: "#23272A"
    readonly property color discode: "#7188D8"
    readonly property color discodeDim: "#2E3859"
    readonly property color staticText: "#A2B7BF"

    readonly property color buttonBackground: "#383C41"
    readonly property color buttonBackgroundHovered: "#3BA55D"
    readonly property color buttonBackgroundDown: buttonBackgroundHovered
    readonly property color buttonBackgroundDim: "#349252"
    readonly property color buttonBackgroundDisabled: "#555555"

    readonly property color buttonText: "#3BA55D"
    readonly property color buttonTextHovered: "#E6F4EA"
    readonly property color buttonTextDown: buttonTextHovered

    readonly property color buttonBorder: buttonText
    readonly property color buttonBorderHovered: buttonTextHovered
    readonly property color buttonBorderDown: buttonTextDown

    readonly property color textFieldBackground: "transparent"
    readonly property color textFieldBackgroundActiveFocus: "transparent"

    readonly property color textFieldText: staticText
    readonly property color textFieldTextActiveFocus: staticText
    readonly property color textFieldPlaceholderText: "#324D6C"

    readonly property color textFieldBorder: discode
    readonly property color textFieldBorderActiveFocus: "#1266F1"
}
