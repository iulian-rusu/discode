#ifndef DISCODE_DESKTOP_BAN_CONTROLLER_H
#define DISCODE_DESKTOP_BAN_CONTROLLER_H

#include "ban_model.h"
#include "ban_service.h"
#include "session_service.h"

#include <memory>

#include <QObject>
#include <QString>

class ban_controller : public QObject {
    Q_OBJECT
public:
    explicit ban_controller(std::shared_ptr<ban_space::ban_service>, std::shared_ptr<session_service>, std::shared_ptr<ban_model>, QObject * = nullptr);

signals:
    void errorOccured();
    void showBanPopup(long long messageId, long long userId);
    void userBanned();
    void userUnbanned();

public slots:
    void onBanClicked(long long, long long);
    void onBan(long long, long long, QString const &);
    void onRefresh();
    void onUnban(long long);

private:
    std::shared_ptr<ban_space::ban_service> bs{};
    std::shared_ptr<session_service> ss{};
    std::shared_ptr<ban_model> bm{};
};


#endif //DISCODE_DESKTOP_BAN_CONTROLLER_H
