#include "report_service.h"

#include "network_helpers.h"

#include <QJsonArray>
#include <QJsonDocument>
#include <QJsonObject>
#include <QNetworkAccessManager>
#include <QNetworkReply>
#include <QObject>

#include <QDebug>

report_space::report_service::report_service(std::shared_ptr<api_config> i_ac) : ac(std::move(i_ac)) { /* Do nothing */ }

void report_space::report_service::review(long long message_id, session sess, report_space::on_success_callback on_success,
                                          report_space::on_failure_callback on_failure) {
    auto network_manager{new QNetworkAccessManager{}};

    network_manager->put(
            network_helpers::get_put_report_request(ac, sess.token),
            network_helpers::get_report_payload(message_id)
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

void report_space::report_service::get(session sess, report_space::on_get_callback on_get, report_space::on_failure_callback on_failure) {
    auto network_manager{new QNetworkAccessManager{}};

    network_manager->get(
            network_helpers::get_report_request(ac, sess.token)
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
                std::vector<report> reports{};

                for (const auto &value : array) {
                    auto const &object = value.toObject();

                    auto message_id = object["messageId"].toInt();
                    auto user_id = object["reported"].toObject()["userId"].toInt();
                    auto message = object["message"].toString().toStdString();
                    auto reason = object["reason"].toString().toStdString();
                    auto issuer = object["reporter"].toObject()["username"].toString().toStdString();

                    reports.emplace_back(message_id, user_id, message, reason, issuer);
                }

                on_get(reports);
            }
    );
}
