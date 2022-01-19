#ifndef DISCODE_DESKTOP_BAN_MODEL_H
#define DISCODE_DESKTOP_BAN_MODEL_H

#include "ban.h"

#include <vector>

#include <QAbstractListModel>
#include <QObject>

class ban_model : public QAbstractListModel {
    Q_OBJECT
public:
    explicit ban_model(QObject * = nullptr);

    [[nodiscard]] int rowCount(QModelIndex const &) const override;
    [[nodiscard]] QVariant data(QModelIndex const &, int) const override;
    [[nodiscard]] QHash<int, QByteArray> roleNames() const override;

    void clear();
    void replaceAll(std::vector<ban>);
    void addOne(ban);
    void removeOne(int);

private:
    std::vector<ban> bans{};
};

#endif //DISCODE_DESKTOP_BAN_MODEL_H
