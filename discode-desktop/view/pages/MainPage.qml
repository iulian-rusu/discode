import com.discode.colors

import "../buttons"

import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

Page {
    id: root

    header: TabBar {
        id: tabBar

        background: Rectangle {
            color: Colors.foreground
        }

        DTabButton {
            id: reportsButton

            implicitHeight: 50

            text: "Reports"

            iconSource: "qrc:/view/resources/icons/flag_black_48dp_normal.svg"
            iconSourceHovered: "qrc:view/resources/icons/flag_black_48dp_dim.svg"
        }

        DTabButton {
            id: bansButton

            implicitHeight: 50

            text: "Bans   "

            iconSource: "qrc:/view/resources/icons/auto_delete_black_48dp_normal.svg"
            iconSourceHovered: "qrc:/view/resources/icons/auto_delete_black_48dp_dim.svg"
        }
    }

    StackLayout {
        id: stackLayout

        anchors.fill: parent

        currentIndex: tabBar.currentIndex

        ReportsPage {
            id: reportsPage
        }

        BansPage {
            id: bansPage
        }
    }
}
