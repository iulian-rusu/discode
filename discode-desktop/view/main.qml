import com.discode.colors 1.0

import "others"

import QtQuick
import QtQuick.Controls
import QtQuick.Layouts
import QtQuick.Window

Window {
    id: root

    width: minimumWidth
    minimumWidth: 1280
    height: minimumHeight
    minimumHeight: 720
    visible: true

    title: "Discode"
    color: Colors.background

    ColumnLayout {
        id: windowLayout

        anchors.fill: parent

        Header {
            id: header

            Layout.fillWidth: true
            Layout.preferredHeight: 80

            logoutVisible: pageView.depth > 1
        }

        StackView {
            id: pageView

            Layout.fillWidth: true
            Layout.fillHeight: true

            Component.onCompleted: {
                // TODO UNCOMMENT push("pages/LoginPage.qml");
                // currentItem.loginSuccessful.connect(onLoginSuccessful);
                push("qrc:/view/pages/MainPage.qml");
            }

            function onLoginSuccessful() {
                push("qrc:/view/pages/MainPage.qml", { "anchors.fill": pageView.StackView.view });
            }

            Connections {
                target: header

                function onLogoutClicked() {
                    pageView.pop();
                }
            }
        }
    }
}
