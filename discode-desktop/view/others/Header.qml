import com.discode.colors 1.0

import "../buttons"
import "../images"

import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

Rectangle {
    id: root

    property bool logoutVisible: true
    signal logoutClicked

    implicitWidth: logo.width + logout.width
    implicitHeight: 80

    color: Colors.headerBackground

    RowLayout {
        id: layout

        anchors.fill: parent

        DiscodeLogo {
            id: logo

            readonly property real widthScale: height / implicitHeight

            Layout.fillHeight: true
            Layout.preferredWidth: implicitWidth * widthScale
            Layout.margins: {
                left: 20
                top: 10
                bottom: 10
            }
        }

        Item {
            id: spacer

            Layout.fillWidth: true
        }

        IconOnlyButton {
            id: logout

            readonly property real preferredSize: 36

            Layout.preferredWidth: preferredSize + padding
            Layout.preferredHeight: preferredSize + padding
            Layout.rightMargin: 10

            visible: root.logoutVisible

            iconSource: "../resources/icons/box-arrow-right-green.svg"
            iconSourceHovered: "../resources/icons/box-arrow-right-white.svg"
            iconWidth: preferredSize
            iconHeight: preferredSize

            onClicked: logoutClicked();
        }
    }
}
