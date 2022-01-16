import QtQuick

DButton {
    id: root

    padding: 15

    displayType: DButton.DisplayType.IconOnly

    radius: width / 2
    borderWidth: 0

    states: State {
        name: "hovered"
        when: root.hovered
        PropertyChanges {
            target: root
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
