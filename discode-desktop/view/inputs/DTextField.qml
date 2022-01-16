import com.discode.colors 1.0
import com.discode.fonts 1.0

import QtQuick
import QtQuick.Controls

TextField {
    id: root

    property color backgroundColor: Colors.textFieldBackground
    property color backgroundActiveFocusColor: Colors.textFieldBackgroundActiveFocus

    property color borderColor: Colors.textFieldBorder
    property color borderActiveFocusColor: Colors.textFieldBorderActiveFocus

    property color textColor: Colors.textFieldText
    property color textActiveFocusColor: Colors.textFieldTextActiveFocus

    property int borderWidth: 1
    property real radius: 5

    leftPadding: 20
    rightPadding: 20

    font.family: Font.regular.font.family
    font.pointSize: 16
    color: root.activeFocus ? root.textActiveFocusColor : root.textColor
    placeholderTextColor: Colors.textFieldPlaceholderText

    selectByMouse: true

    background: Rectangle {
        id: buttonBackground

        border.width: root.borderWidth
        radius: root.radius

        color: root.activeFocus ? root.backgroundActiveFocusColor : root.backgroundColor
        border.color: root.activeFocus ? root.borderActiveFocusColor : root.borderColor
    }
}
