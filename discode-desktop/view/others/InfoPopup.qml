import com.discode.colors 1.0

import "../buttons"
import "../labels"

import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

Popup {
    id: root

    signal accepted(int duration, string reason)
    signal rejected

    property string title: "Title"

    onRejected: root.close();

    width: popupBackground.width + 2 * padding
    height: popupBackground.height + 2 * padding

    modal: true

    Rectangle {
        id: popupBackground

        readonly property real padding: 40

        width: layout.width + 2 * padding
        height: layout.height + 2 * padding

        color: Colors.background

        GridLayout {
            id: layout

            anchors.centerIn: parent
            width: 300

            columns: 2
            rowSpacing: 20
            columnSpacing: 10

            DText {
                id: titleText

                Layout.columnSpan: 2
                Layout.alignment: Qt.AlignVCenter
                Layout.fillWidth: true
                Layout.bottomMargin: 50

                text: root.title
                font.pointSize: 22
                color: Colors.discode
                horizontalAlignment: Text.AlignHCenter
            }

            DButton {
                id: okButton

                Layout.fillWidth: true

                displayType: DButton.DisplayType.TextOnly

                text: "Ok"

                onClicked: root.accepted(parseInt(durationInput.text), reasonInput.text)
            }

            DButton {
                id: cancelButton

                Layout.fillWidth: true

                displayType: DButton.DisplayType.TextOnly

                text: "Cancel"

                onClicked: root.rejected()
            }
        }
    }
}
