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

    ListModel {
        id: bansModel

        ListElement {
            banId: 0
            user: "billie123"
            start: "22.10.2014 22:23"
            end: "22.10.2014 22:238"
            reason: "Bill SmithBillBill SmithBillBill SmithBillBill SmithBillBill Bill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillSmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill Smith"
        }
    }
}
