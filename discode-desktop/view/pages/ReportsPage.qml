import com.discode.colors 1.0

import "../lists"
import "../others"

import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

Page {
    id: root

    ListView {
        id: listView

        anchors {
            fill: parent
            topMargin: 2
            bottomMargin: 2
        }

        spacing: 2
        clip: true

        model: reportModel

        delegate: Component {
            id: delegate

            Loader {
                id: loader

                width: listView.width

                source: "qrc:/view/lists/ReportDelegate.qml"
            }
        }
    }

    BanPopup {
        id: banPopup

        property int messageId: -1
        property int userId: -1

        anchors.centerIn: parent

        title: "Ban details"

        onAccepted: (duration, reason) => {
            banPopup.cleanClose();
            banController.onBan(messageId, userId, duration, reason);
        }
    }

    Connections {
        target: banController
        function onShowBanPopup(messageId, userId) {
            banPopup.messageId = messageId;
            banPopup.userId = userId;
            banPopup.open();
        }
    }
}
