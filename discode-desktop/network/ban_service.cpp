#include "ban_service.h"

#include "network_helpers.h"

#include <QJsonArray>
#include <QJsonDocument>
#include <QJsonObject>
#include <QNetworkAccessManager>
#include <QNetworkReply>
#include <QObject>

#include <QDebug>

ban_space::ban_service::ban_service(std::shared_ptr<api_config> i_ac) : ac(std::move(i_ac)) { /* Do nothing */ }

void ban_space::ban_service::ban_user(long long user_id, long long seconds, std::string reason, session sess, on_success_callback on_success, on_failure_callback on_failure) {
    auto network_manager{new QNetworkAccessManager{}};

    network_manager->post(
            network_helpers::get_ban_request(ac, sess.token),
            network_helpers::get_ban_payload(user_id, seconds, reason)
    );

    QObject::connect(
            network_manager,
            &QNetworkAccessManager::finished,
            [
                    this,
                    network_manager = std::make_unique<QNetworkAccessManager>(network_manager),
                    on_success = std::move(on_success),
                    on_failure = std::move(on_failure)
            ](QNetworkReply *reply) {
                if (reply->error() != QNetworkReply::NoError) {
                    on_failure();
                    return;
                }

                on_success();
            }
    );
}

void ban_space::ban_service::get(session sess, on_get_callback on_get, on_failure_callback on_failure) {
    auto network_manager{new QNetworkAccessManager{}};

    network_manager->get(
            network_helpers::get_ban_request(ac, sess.token)
    );

    QObject::connect(
            network_manager,
            &QNetworkAccessManager::finished,
            [
                    this,
                    network_manager = std::make_unique<QNetworkAccessManager>(network_manager),
                    on_get = std::move(on_get),
                    on_failure = std::move(on_failure)
            ](QNetworkReply *reply) {
                if (reply->error() != QNetworkReply::NoError) {
                    on_failure();
                    return;
                }

                auto const json_document{QJsonDocument::fromJson(reply->readAll())};
                auto const array = json_document.array();
                std::vector<ban> bans{};

                for (const auto &value : array) {
                    auto const &object = value.toObject();

                    auto ban_id = object["banId"].toInt();
                    auto user = object["user"].toObject()["username"].toString().toStdString();
                    auto start = object["startDate"].toString().toStdString();
                    auto end = object["endDate"].toString().toStdString();
                    auto reason = object["reason"].toString().toStdString();

                    start = start.substr(0, start.size() - 10);
                    end = end.substr(0, end.size() - 10);

                    bans.emplace_back(ban_id, user, start, end, reason);
                }

                on_get(bans);
            }
    );
}

void ban_space::ban_service::unban(long long user_id, session sess, on_success_callback on_success, on_failure_callback on_failure) {
    auto network_manager{new QNetworkAccessManager{}};

    network_manager->deleteResource(
            network_helpers::get_unban_request(ac, sess.token, user_id)
    );

    QObject::connect(
            network_manager,
            &QNetworkAccessManager::finished,
            [
                    this,
                    network_manager = std::make_unique<QNetworkAccessManager>(network_manager),
                    on_success = std::move(on_success),
                    on_failure = std::move(on_failure)
            ](QNetworkReply *reply) {
                qDebug() << reply->error();
                if (reply->error() != QNetworkReply::NoError) {
                    on_failure();
                    return;
                }

                on_success();
            }
    );
}
