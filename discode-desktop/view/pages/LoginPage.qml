import com.discode.colors 1.0

import "../buttons"
import "../inputs"
import "../labels"

import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

Page {
    id: root

    function loginClicked() {
        if (usernameField.text === "" || passwordField.text === "")
            return;

        login.enabled = false;

        authenticationController.onAuthenticationRequested(usernameField.text, passwordField.text);
    }

    Rectangle {
        id: loginSection

        readonly property real padding: 80

        anchors.centerIn: parent
        implicitWidth: layout.width + padding
        implicitHeight: layout.height + padding

        color: Colors.foreground

        ColumnLayout {
            id: layout

            anchors.centerIn: parent

            DTextEdit {
                id: description

                Layout.bottomMargin: 80

                text: "Discode administration platform"
                font.pointSize: 22
                color: Colors.discode
            }

            DTextEdit {
                id: usernameLabel

                Layout.bottomMargin: 15

                text: "Username"
            }

            DTextField {
                id: usernameField

                Layout.preferredHeight: 50
                Layout.fillWidth: true
                Layout.bottomMargin: 20

                placeholderText: "Username"

                onAccepted: root.loginClicked();
            }

            DTextEdit {
                id: passwordLabel

                Layout.bottomMargin: 15

                text: "Password"
            }

            SecretField {
                id: passwordField

                Layout.preferredHeight: 50
                Layout.fillWidth: true
                Layout.bottomMargin: 40

                placeholderText: "Password"

                onAccepted: root.loginClicked();
            }

            DButton {
                id: login

                Layout.preferredHeight: 50
                Layout.fillWidth: true

                enabled: usernameField.text !== "" && passwordField.text !== ""

                displayType: DButton.DisplayType.TextOnly

                borderWidth: 0

                text: "LOG IN"

                backgroundColor: Colors.buttonBackgroundHovered
                backgroundColorHovered: Colors.buttonBackgroundDim
                backgroundColorDown: Colors.buttonBackgroundDim
                textColor: Colors.buttonTextHovered

                onClicked: root.loginClicked();
            }
        }
    }

    function resetPage() {
        usernameField.clear();
        passwordField.clear();
        login.enabled = true;
    }

    Connections {
        target: authenticationController

        function onAuthenticationError() {
            resetPage();
            console.log("Authentication failed"); // TODO Open popup
        }

        function onAuthenticated() {
            resetPage();
        }
    }
}
