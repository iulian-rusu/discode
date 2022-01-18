#include "ban_model.h"

#include "ban_model_roles.h"

ban_model::ban_model(QObject *parent) : QAbstractListModel(parent) { /* Do nothing */ }

int ban_model::rowCount(const QModelIndex &index) const {
    if (index.isValid()) return 0;
    return bans.size();
}

QVariant ban_model::data(const QModelIndex &index, int role) const {
    if (!index.isValid()) return {};

    try {
        auto const &ban{bans.at(index.row())};

        switch (role) {
            case static_cast<int>(ban_model_roles::BanId):
                return { ban.banId };
            case static_cast<int>(ban_model_roles::User):
                return { QString::fromStdString(ban.user) };
            case static_cast<int>(ban_model_roles::Start):
                return { QString::fromStdString(ban.start) };
            case static_cast<int>(ban_model_roles::End):
                return { QString::fromStdString(ban.end) };
            case static_cast<int>(ban_model_roles::Reason):
                return { QString::fromStdString(ban.reason) };
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

void ban_model::clear() {
    beginRemoveRows({}, 0, rowCount({}) - 1);
    bans.clear();
    endRemoveRows();
}

void ban_model::replaceAll(std::vector<ban> &&new_bans) {
    clear();

    beginInsertRows({}, 0, new_bans.size() - 1);
    bans.swap(new_bans);
    endInsertRows();
}

void ban_model::addOne(ban &&new_ban) {
    beginInsertRows({}, rowCount({}), rowCount({}));
    bans.emplace_back(new_ban);
    endInsertRows();
}

void ban_model::removeOne(int row) {
    if (row < 0 || row >= bans.size()) return;
    beginRemoveRows({}, row, row);
    auto const item_iterator = bans.begin() + row;
    bans.erase(item_iterator, item_iterator + 1);
    endInsertRows();
}
