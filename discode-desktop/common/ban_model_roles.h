#ifndef DISCODE_DESKTOP_BAN_MODEL_ROLES_H
#define DISCODE_DESKTOP_BAN_MODEL_ROLES_H

#include <QAbstractListModel>

enum class ban_model_roles {
    BanId = Qt::UserRole + 1,
    User,
    Start,
    End,
    Reason,
};

#endif //DISCODE_DESKTOP_BAN_MODEL_ROLES_H
