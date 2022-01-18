#include "ban_model.h"

#include "ban_model_roles.h"

#include <QDebug>

ban_model::ban_model(QObject *parent) : QAbstractListModel(parent) {
    /* Do nothing */
    std::vector<ban> initial_bans {
        ban{
            .banId = 0,
            .user = "billie123",
            .start = "22.10.2014 22:23",
            .end = "22.10.2014 22:238",
            .reason = "Bill SmithBillBill SmithBillBill SmithBillBill SmithBillBill Bill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillSmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill Smith"
        }
    };
    replaceAll(std::move(initial_bans));
    addOne(ban{
            .banId = 2,
            .user = "billie123",
            .start = "22.10.2014 22:23",
            .end = "22.10.2014 22:238",
            .reason = "Bill SmithBillBill SmithBillBill SmithBillBill SmithBillBill Bill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillSmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBillBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill SmithBill Smith"
    });
}

int ban_model::rowCount(const QModelIndex &index) const {
    if (!index.isValid()) return 0;
    return bans.size();
}

QVariant ban_model::data(const QModelIndex &index, int role) const {
    if (!index.isValid()) return {};
    qDebug() << "Got data";

    try {
        auto const &ban{bans.at(index.row())};

        switch (role) {
            case static_cast<int>(ban_model_roles::BanId):
                return QVariant::fromValue(ban.banId);
            case static_cast<int>(ban_model_roles::User):
                return QVariant::fromValue(ban.user);
            case static_cast<int>(ban_model_roles::Start):
                return QVariant::fromValue(ban.start);
            case static_cast<int>(ban_model_roles::End):
                return QVariant::fromValue(ban.end);
            case static_cast<int>(ban_model_roles::Reason):
                return QVariant::fromValue(ban.reason);
            default: /* Do nothing */ ;
        }
    }
    catch (std::out_of_range &exception) { /* Do nothing */ }

    return {};
}

QHash<int, QByteArray> ban_model::roleNames() const {
    QHash<int, QByteArray> roles;

    roles[static_cast<int>(ban_model_roles::BanId)] = "banId";
    roles[static_cast<int>(ban_model_roles::User)] = "user";
    roles[static_cast<int>(ban_model_roles::Start)] = "start";
    roles[static_cast<int>(ban_model_roles::End)] = "end";
    roles[static_cast<int>(ban_model_roles::Reason)] = "reason";

    return roles;
}
/*
Qt::ItemFlags ban_model::flags(const QModelIndex &index) const {
    return QAbstractListModel::flags(index);
}
*/
void ban_model::clear() {
    beginRemoveRows({}, 0, rowCount({}) - 1);
    bans.clear();
    endRemoveRows();
    qDebug() << "Cleared rows";
}

void ban_model::replaceAll(std::vector<ban> &&new_bans) {
    clear();
    beginInsertRows({}, 0, new_bans.size() - 1);
    bans.swap(new_bans);
    endInsertRows();
    qDebug() << "Replaced rows";
}

void ban_model::addOne(ban &&new_ban) {
    beginInsertRows({}, rowCount({}), rowCount({}));
    bans.emplace_back(new_ban);
    endInsertRows();
    qDebug() << "Added row";
}

void ban_model::removeOne(int row) {
    if (row < 0 || row >= bans.size()) return;
    beginRemoveRows({}, row, row);
    auto const item_iterator = bans.begin() + row;
    bans.erase(item_iterator, item_iterator + 1);
    endInsertRows();
    qDebug() << "Removed row";
}
