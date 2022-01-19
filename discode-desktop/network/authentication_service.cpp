#include "authentication_service.h"

#include "network_helpers.h"

#include <utility>

#include <QJsonDocument>
#include <QNetworkAccessManager>
#include <QNetworkReply>
#include <QObject>

authentication_service::authentication_service(api_config i_ac) : ac(i_ac) { /* Do nothing */ }

void authentication_service::authenticate(std::string username, std::string password, on_success_callback on_success, on_failure_callback on_failure) {
    auto network_manager{new QNetworkAccessManager{}};

    network_manager->post(
            network_helpers::get_authentication_request(ac),
            network_helpers::get_authentication_payload(std::move(username), std::move(password))
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

                auto jwt = QJsonDocument::fromJson(reply->readAll())["token"].toString().toStdString();
                on_success(std::move(jwt));
            }
    );
}
