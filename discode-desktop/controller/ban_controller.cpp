#include "ban_controller.h"

#include <string>

#include <QDebug>

ban_controller::ban_controller(std::shared_ptr<ban_space::ban_service> i_bs, std::shared_ptr<session_service> i_ss, std::shared_ptr<ban_model> i_bm, QObject *parent)
    : QObject(parent), bs(std::move(i_bs)), ss(std::move(i_ss)), bm(std::move(i_bm)) { }

void ban_controller::onBanClicked(long long messageId, long long userId) {
    emit showBanPopup(messageId, userId);
}

void ban_controller::onBan(long long userId, long long seconds, QString const &reason) {
    auto const &session = ss->get_session();

    auto on_success = [this]() {
        onRefresh();
        emit userBanned();
    };
    auto on_failure = [this]() {
        emit errorOccured(); // Show popup
    };
    bs->ban_user(userId, seconds, reason.toStdString(), session.value(), on_success, on_failure);
}

void ban_controller::onRefresh() {
    auto const &session = ss->get_session();

    auto on_get = [this](std::vector<ban> bans) {
        bm->replaceAll(bans);
    };
    auto on_failure = [this]() {
        emit errorOccured();
    };
    bs->get(session.value(), on_get, on_failure);
}

void ban_controller::onUnban(long long ban_id) {
    auto const &session = ss->get_session();

    auto on_success = [this]() {
        onRefresh();
        emit userUnbanned();
    };
    auto on_failure = [this]() {
        emit errorOccured();
    };
    bs->unban(ban_id, session.value(), on_success, on_failure);
}
