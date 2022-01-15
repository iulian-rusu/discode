pragma Singleton

import QtQuick

Item {
    readonly property color windowBackground: "#2C2F33"
    readonly property color headerBackground: "#23272A"

    readonly property color buttonBackground: "#383C41"
    readonly property color buttonBackgroundHovered: "#3BA55D"
    readonly property color buttonBackgroundDown: buttonBackgroundHovered
    readonly property color buttonText: "#3BA55D"
    readonly property color buttonTextHovered: "#E6F4EA"
    readonly property color buttonTextDown: buttonTextHovered
    readonly property color buttonBorder: buttonText
    readonly property color buttonBorderHovered: buttonTextHovered
    readonly property color buttonBorderDown: buttonTextDown
}
