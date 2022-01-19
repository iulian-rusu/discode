#include "report_model.h"

#include "report_model_roles.h"

report_model::report_model(QObject *parent) : QAbstractListModel(parent) { /* Do nothing */ }

int report_model::rowCount(const QModelIndex &index) const {
    if (index.isValid()) return 0;
    return reports.size();
}

QVariant report_model::data(const QModelIndex &index, int role) const {
    if (!index.isValid()) return {};

    try {
        auto const &report{reports.at(index.row())};

        switch (role) {
            case static_cast<int>(report_model_roles::MessageId):
                return { report.messageId };
            case static_cast<int>(report_model_roles::UserId):
                return { report.userId };
            case static_cast<int>(report_model_roles::Message):
                return { QString::fromStdString(report.message) };
            case static_cast<int>(report_model_roles::Issuer):
                return { QString::fromStdString(report.issuer) };
            default: /* Do nothing */ ;
        }
    }
    catch (std::out_of_range &exception) { /* Do nothing */ }

    return {};
}

QHash<int, QByteArray> report_model::roleNames() const {
    QHash<int, QByteArray> roles;

    roles[static_cast<int>(report_model_roles::MessageId)] = "messageId";
    roles[static_cast<int>(report_model_roles::UserId)] = "userId";
    roles[static_cast<int>(report_model_roles::Message)] = "message";
    roles[static_cast<int>(report_model_roles::Issuer)] = "issuer";

    return roles;
}

void report_model::clear() {
    beginRemoveRows({}, 0, rowCount({}) - 1);
    reports.clear();
    endRemoveRows();
}

void report_model::replaceAll(std::vector<report> new_reports) {
    clear();

    beginInsertRows({}, 0, new_reports.size() - 1);
    reports.swap(new_reports);
    endInsertRows();
}

void report_model::addOne(report new_report) {
    beginInsertRows({}, rowCount({}), rowCount({}));
    reports.emplace_back(new_report);
    endInsertRows();
}

void report_model::removeOne(int row) {
    if (row < 0 || row >= reports.size()) return;
    beginRemoveRows({}, row, row);
    auto const item_iterator = reports.begin() + row;
    reports.erase(item_iterator, item_iterator + 1);
    endInsertRows();
}
