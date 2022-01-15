pragma Singleton

import QtQuick

Item {
    readonly property FontLoader font: fontLoader.font

    FontLoader {
        id: fontLoader

        source: "../resources/fonts/JetBrainsMono-Regular.ttf"
    }
}