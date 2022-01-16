import com.discode.colors 1.0

import "../buttons"
import "../labels"
import "../inputs"

import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

Popup {
    id: root

    signal accepted(int duration, string reason)
    signal rejected

    property string title: "Title"

    onRejected: cleanClose();

    width: popupBackground.width + 2 * padding
    height: popupBackground.height + 2 * padding

    modal: true

    function cleanClose() {
        root.close();
        durationInput.text = "";
        reasonInput.text = "";
    }

    Rectangle {
        id: popupBackground

        readonly property real padding: 40

        width: layout.width + 2 * padding
        height: layout.height + 2 * padding

        color: Colors.background

        GridLayout {
            id: layout

            anchors.centerIn: parent
            width: 500

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

            DText {
                id: durationLabel

                Layout.columnSpan: 2
                Layout.fillWidth: true

                text: "Duration"
            }

            DTextField {
                id: durationInput

                Layout.columnSpan: 2
                Layout.fillWidth: true
                Layout.bottomMargin: 10

                placeholderText: "Duration"
            }

            DText {
                id: reasonLabel

                Layout.columnSpan: 2
                Layout.fillWidth: true

                text: "Reason"
            }

            DTextField {
                id: reasonInput

                Layout.columnSpan: 2
                Layout.fillWidth: true
                Layout.preferredHeight: 200
                Layout.bottomMargin: 30

                placeholderText: "Reason"

                wrapMode: TextInput.WordWrap
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
