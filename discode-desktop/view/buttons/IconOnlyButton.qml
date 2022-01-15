import QtQuick

DButton {
    id: root

    padding: 15

    displayType: DButton.DisplayType.IconOnly

    radius: width / 2
    borderWidth: 0

    states: State {
        name: "hovered"
        when: logout.hovered
        PropertyChanges {
            target: logout
            radius: width / 4
        }
    }

    transitions: Transition {
        NumberAnimation {
            properties: "radius"
            easing.type: Easing.InOutQuad
        }
    }
}
