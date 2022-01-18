#ifndef DISCODE_DESKTOP_REPORT_MODEL_ROLES_H
#define DISCODE_DESKTOP_REPORT_MODEL_ROLES_H

#include <QAbstractListModel>

enum class report_model_roles {
    MessageId = Qt::UserRole + 1,
    UserId,
    Message,
    Issuer,
};

#endif //DISCODE_DESKTOP_REPORT_MODEL_ROLES_H
