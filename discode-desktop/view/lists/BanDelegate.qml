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

    property real verticalPadding: 6

    implicitWidth: 500
    implicitHeight: 200
    height: mouseArea.height + 2 * verticalPadding

    border.width: 2

    color: mouseArea.down ? backgroundColorDown :
            mouseArea.hovered ? backgroundColorHovered : backgroundColor
    border.color: mouseArea.down ? borderColorDown :
                    mouseArea.hovered ? borderColorHovered : borderColor

    MouseArea {
        id: mouseArea

        readonly property bool hovered: containsMouse || unban.hovered

        anchors {
            left: parent.left
            right: parent.right
            top: parent.top
            topMargin: root.verticalPadding
        }

        height: layout.height

        hoverEnabled: true
        preventStealing: true

        GridLayout {
            id: layout

            readonly property real horizontalPadding: 20
            readonly property real firstColumnWidth: 300
            readonly property real labelHeight: 29
            readonly property real separatorWidth: root.border.width - 1

            anchors {
                left: parent.left
                right: parent.right
                leftMargin: horizontalPadding
                rightMargin: horizontalPadding
            }

            flow: GridLayout.TopToBottom
            rows: 3
            columns: 5
            columnSpacing: 20
            rowSpacing: 3

            DText {
                id: userLabel

                Layout.preferredWidth: layout.firstColumnWidth
                Layout.preferredHeight: layout.labelHeight

                text: user
                font.pointSize: root.pointSize
                color: Colors.buttonTextHovered
            }

            DText {
                id: startLabel

                Layout.preferredWidth: layout.firstColumnWidth
                Layout.preferredHeight: layout.labelHeight

                text: `Start: ${start}`
                font.pointSize: root.pointSize
                color: Colors.buttonTextHovered
            }

            DText {
                id: endLabel

                Layout.preferredWidth: layout.firstColumnWidth
                Layout.preferredHeight: layout.labelHeight

                text: `End: ${end}`
                font.pointSize: root.pointSize
                color: Colors.buttonTextHovered
            }

            Rectangle {
                id: firstToSecondSeparator

                Layout.rowSpan: layout.rows
                Layout.preferredWidth: layout.separatorWidth
                Layout.fillHeight: true

                color: root.border.color
            }

            DText {
                id: reasonLabel

                Layout.rowSpan: layout.rows
                Layout.preferredWidth: layout.width - layout.firstColumnWidth - unban.width - 2 * layout.separatorWidth - (layout.columns - 1) * layout.columnSpacing
                Layout.fillHeight: true

                text: `Reason: ${reason}`

                wrapMode: Text.WordWrap

                font.pointSize: root.pointSize
                color: Colors.buttonTextHovered
            }

            Rectangle {
                id: secondToThirdSeparator

                Layout.rowSpan: layout.rows
                Layout.preferredWidth: layout.separatorWidth
                Layout.fillHeight: true

                color: root.border.color
            }

            Text {
                id: spacer
            }

            IconOnlyButton {
                id: unban

                readonly property real preferredSize: 50 - padding

                Layout.preferredWidth: preferredSize + padding
                Layout.preferredHeight: preferredSize + padding

                backgroundColorHovered: Colors.buttonBackgroundDisabled
                backgroundColorDown: backgroundColorHovered
                iconSource: "qrc:/view/resources/icons/delete_sweep_black_48dp_normal.svg"
                iconSourceHovered: "qrc:/view/resources/icons/delete_sweep_black_48dp_dim.svg"
                iconWidth: preferredSize
                iconHeight: preferredSize

                onClicked: banController.onUnban(banId);
            }
        }
    }
}
