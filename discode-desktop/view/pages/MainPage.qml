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

            text: "Reports"
        }

        DTabButton {
            id: bansButton

            text: "Bans"
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
    Component.onCompleted: {
        console.log(root);
        console.log(bansPage.parent);
        console.log(header.parent);
        console.log(contentItem);
    }
}
