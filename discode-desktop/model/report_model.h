#ifndef DISCODE_DESKTOP_REPORT_MODEL_H
#define DISCODE_DESKTOP_REPORT_MODEL_H

#include "report.h"

#include <vector>

#include <QAbstractListModel>
#include <QObject>

class report_model : public QAbstractListModel {
Q_OBJECT
public:
    explicit report_model(QObject * = nullptr);

    [[nodiscard]] int rowCount(QModelIndex const &) const override;
    [[nodiscard]] QVariant data(QModelIndex const &, int) const override;
    [[nodiscard]] QHash<int, QByteArray> roleNames() const override;

    void clear();
    void replaceAll(std::vector<report> &&);
    void addOne(report &&);
    void removeOne(int);

private:
    std::vector<report> reports{};
};

#endif //DISCODE_DESKTOP_REPORT_MODEL_H
