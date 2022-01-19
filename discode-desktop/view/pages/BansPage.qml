import com.discode.colors 1.0

import "../lists"

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

        model: banModel

        delegate: Component {
            id: delegate

            Loader {
                id: loader

                width: listView.width

                source: "qrc:/view/lists/BanDelegate.qml"
            }
        }
    }
}
