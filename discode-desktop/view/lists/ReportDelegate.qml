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

        readonly property bool hovered: containsMouse || review.hovered || ban.hovered

        anchors {
            left: parent.left
            right: parent.right
            top: parent.top
            topMargin: root.verticalPadding
        }

        height: layout.height

        hoverEnabled: true
        propagateComposedEvents: true

        GridLayout {
            id: layout

            readonly property real horizontalPadding: 20
            readonly property real verticalPadding: 10
            readonly property real secondColumnWidth: 400
            readonly property real buttonsWidth: 50
            readonly property real labelHeight: 29
            readonly property real separatorWidth: root.border.width - 1

            anchors {
                left: parent.left
                right: parent.right
                leftMargin: horizontalPadding
                rightMargin: horizontalPadding
            }

            flow: GridLayout.TopToBottom
            rows: 2
            columns: 5
            columnSpacing: 20
            rowSpacing: 3

            DText {
                id: messageLabel

                Layout.rowSpan: layout.rows
                Layout.preferredWidth:  layout.width - layout.secondColumnWidth - layout.buttonsWidth - 2 * layout.separatorWidth - (layout.columns - 1) * layout.columnSpacing
                Layout.fillHeight: true

                text: `Message: ${message}`

                wrapMode: Text.WordWrap

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
                id: issuerLabel

                Layout.preferredWidth: layout.secondColumnWidth
                Layout.preferredHeight: layout.labelHeight

                text: `Issuer: ${issuer}`
                font.pointSize: root.pointSize
                color: Colors.buttonTextHovered
            }

            DText {
                id: reasonLabel

                Layout.preferredWidth: layout.secondColumnWidth
                Layout.preferredHeight: layout.labelHeight

                text: `Reason: ${reason}`
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

            IconOnlyButton {
                id: review

                readonly property real preferredSize: layout.buttonsWidth - padding

                Layout.preferredWidth: preferredSize + padding
                Layout.preferredHeight: preferredSize + padding

                backgroundColorHovered: Colors.buttonBackgroundDisabled
                backgroundColorDown: backgroundColorHovered
                iconSource: "qrc:/view/resources/icons/done_black_48dp_normal.svg"
                iconSourceHovered: "qrc:/view/resources/icons/done_black_48dp_dim.svg"
                iconWidth: preferredSize
                iconHeight: preferredSize

                onClicked: reportController.onReview(messageId);
            }

            IconOnlyButton {
                id: ban

                readonly property real preferredSize: layout.buttonsWidth - padding

                Layout.preferredWidth: preferredSize + padding
                Layout.preferredHeight: preferredSize + padding

                backgroundColorHovered: Colors.buttonBackgroundDisabled
                backgroundColorDown: backgroundColorHovered
                iconSource: "qrc:/view/resources/icons/restore_from_trash_black_48dp_normal.svg"
                iconSourceHovered: "qrc:/view/resources/icons/restore_from_trash_black_48dp_dim.svg"
                iconWidth: preferredSize
                iconHeight: preferredSize

                onClicked: banController.onBanClicked(messageId, userId);
            }
        }
    }
}
