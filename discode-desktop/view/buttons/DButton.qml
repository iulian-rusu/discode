import com.discode.colors 1.0
import com.discode.fonts 1.0

import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

Button {
    id: root

    enum DisplayType {
        TextOnly,
        IconOnly,
        TextBeforeIcon,
        TextAfterIcon,
        TextAboveIcon,
        TextBelowIcon
    }

    property color backgroundColor: Colors.buttonBackground
    property color backgroundColorHovered: Colors.buttonBackgroundHovered
    property color backgroundColorDown: Colors.buttonBackgroundDown
    property color textColor: Colors.buttonText
    property color textColorHovered: Colors.buttonTextHovered
    property color textColorDown: Colors.buttonTextDown
    property color borderColor: Colors.buttonBorder
    property color borderColorHovered: Colors.buttonBorderHovered
    property color borderColorDown: Colors.buttonBorderDown

    property url iconSource: ""
    property url iconSourceHovered: ""
    property url iconSourceDown: iconSourceHovered

    readonly property bool textVisible: displayType !== DButton.DisplayType.IconOnly
    readonly property bool iconVisible: displayType !== DButton.DisplayType.TextOnly

    property int displayType: DButton.DisplayType.TextAfterIcon
    readonly property int textRow: displayType === DButton.DisplayType.TextBelowIcon ? 1 : 0
    readonly property int textColumn: displayType === DButton.DisplayType.TextAfterIcon ? 1 : 0
    readonly property int iconRow: displayType === DButton.DisplayType.TextAboveIcon ? 1 : 0
    readonly property int iconColumn: displayType === DButton.DisplayType.TextBeforeIcon ? 1 : 0
    property int contentSpacing: 5

    property real pointSize: 12

    property real iconWidth: buttonIcon.implicitWidth
    property real iconHeight: buttonIcon.implicitHeight
    property int iconFillMode: Image.PreserveAspectFit

    property int borderWidth: 1
    property real radius: 10

    clip: true
    flat: true
    highlighted: true

    text: "Placeholder"

    background: Rectangle {
        id: buttonBackground

        border.width: root.borderWidth
        radius: root.radius

        color: root.down ? root.backgroundColorDown :
                root.hovered ? root.backgroundColorHovered : root.backgroundColor
        border.color: root.down ? root.borderColorDown :
                root.hovered ? root.borderColorHovered : root.borderColor
    }

    contentItem: GridLayout {
        id: buttonContentItem

        rows: 3
        columns: 3
        rowSpacing: root.contentSpacing
        columnSpacing: root.contentSpacing

        Text {
            id: buttonText

            readonly property bool notOnIconColumn: root.textColumn !== root.iconColumn
            readonly property real deductionFromMaxWidth: root.iconVisible && notOnIconColumn ? buttonIcon.paintedWidth + parent.columnSpacing : 0
            readonly property real maximumWidth: parent.width - deductionFromMaxWidth
            readonly property bool notOnIconRow: root.textRow !== root.iconRow
            readonly property real deductionFromMaxHeight: root.iconVisible && notOnIconRow ? buttonIcon.paintedHeight + parent.rowSpacing : 0
            readonly property real maximumHeight: parent.height - deductionFromMaxHeight

            Layout.row: root.textRow
            Layout.column: root.textColumn
            Layout.alignment: Qt.AlignHCenter | Qt.AlignVCenter
            Layout.maximumWidth: maximumWidth > 0 ? maximumWidth : 0
            Layout.maximumHeight: maximumHeight > 0 ? maximumHeight : 0

            visible: root.textVisible

            text: root.text
            elide: Text.ElideRight
            font: Font.regular.font
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
            color: root.down ? root.textColorDown :
                    root.hovered ? root.textColorHovered : root.textColor
        }

        Image {
            id: buttonIcon

            Layout.row: root.iconRow
            Layout.column: root.iconColumn
            Layout.alignment: Qt.AlignHCenter | Qt.AlignVCenter
            Layout.preferredWidth: root.iconWidth
            Layout.preferredHeight: root.iconHeight
            Layout.maximumWidth: parent.width
            Layout.maximumHeight: parent.height

            visible: root.iconVisible

            source: root.down ? root.iconSourceDown:
                        root.hovered ? root.iconSourceHovered : root.iconSource
            fillMode: root.iconFillMode
        }
    }
}
