import com.discode.colors 1.0

import "../buttons"
import "../labels"

import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

Rectangle {
    id: root

    property color backgroundColor: Colors.buttonBackground
    property color backgroundColorHovered: Colors.foreground
    property color backgroundColorDown: backgroundColorHovered

    property color borderColor: Colors.discode
    property color borderColorHovered: Colors.discodeDim
    property color borderColorDown: borderColorHovered

    property real pointSize: 12

    implicitWidth: 500
    implicitHeight: 200
    height: mouseArea.height

    border.width: 2

    color: mouseArea.down ? backgroundColorDown :
            mouseArea.hovered ? backgroundColorHovered : backgroundColor
    border.color: mouseArea.down ? borderColorDown :
                    mouseArea.hovered ? borderColorHovered : borderColor

    MouseArea {
        id: mouseArea

        readonly property bool hovered: containsMouse

        anchors {
            left: parent.left
            right: parent.right
        }

        height: layout.height

        hoverEnabled: true
        preventStealing: true

        GridLayout {
            id: layout

            readonly property real horizontalPadding: 20
            readonly property real verticalPadding: 6
            readonly property real firstColumnWidth: 300
            readonly property real rowHeight: 29

            anchors {
                left: parent.left
                right: parent.right
                leftMargin: horizontalPadding
                rightMargin: horizontalPadding
            }

            columns: 3
            rows: 3
            columnSpacing: 20
            rowSpacing: 3

            DText {
                id: userLabel

                Layout.column: 0
                Layout.row: 0
                Layout.preferredWidth: layout.firstColumnWidth
                Layout.preferredHeight: layout.rowHeight
                Layout.topMargin: verticalPadding

                text: user
                font.pointSize: root.pointSize
                color: Colors.buttonTextHovered
            }

            DText {
                id: startLabel

                Layout.column: 0
                Layout.row: 1
                Layout.preferredWidth: layout.firstColumnWidth
                Layout.preferredHeight: layout.rowHeight

                text: `Start: ${start}`
                font.pointSize: root.pointSize
                color: Colors.buttonTextHovered
            }

            DText {
                id: endLabel

                Layout.column: 0
                Layout.row: 2
                Layout.preferredWidth: layout.firstColumnWidth
                Layout.preferredHeight: layout.rowHeight

                text: `End: ${end}`
                font.pointSize: root.pointSize
                color: Colors.buttonTextHovered
                Layout.bottomMargin: verticalPadding
            }

            DText {
                id: reasonLabel

                Layout.column: 1
                Layout.row: 0
                Layout.rowSpan: 3
                Layout.preferredWidth: layout.width - layout.firstColumnWidth - unban.width - 2 * layout.columnSpacing
                Layout.fillHeight: true
                Layout.topMargin: verticalPadding
                Layout.bottomMargin: verticalPadding

                text: `Reason: ${reason}`

                wrapMode: Text.WordWrap

                font.pointSize: root.pointSize
                color: Colors.buttonTextHovered
            }

            IconOnlyButton {
                id: unban

                readonly property real preferredSize: 50 - padding

                Layout.column: 2
                Layout.row: 1
                Layout.preferredWidth: preferredSize + padding
                Layout.preferredHeight: preferredSize + padding

                backgroundColorHovered: Colors.buttonBackgroundDisabled
                backgroundColorDown: backgroundColorHovered
                iconSource: "qrc:/view/resources/icons/delete_sweep_black_48dp_dim.svg"
                iconSourceHovered: "qrc:/view/resources/icons/delete_sweep_black_48dp_normal.svg"
                iconWidth: preferredSize
                iconHeight: preferredSize

                // TODO Call controller function
                onClicked: banController.onUnban(banId);
            }
        }
    }
}
