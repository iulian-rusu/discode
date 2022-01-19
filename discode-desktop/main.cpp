#include "api_config.h"
#include "authentication_controller.h"
#include "authentication_service.h"
#include "ban_controller.h"
#include "ban_model.h"
#include "ban_service.h"
#include "report_controller.h"
#include "report_model.h"
#include "session_service.h"

#include <utility>

#include <QGuiApplication>
#include <QIcon>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include <QString>

static_assert(QT_VERSION >= QT_VERSION_CHECK(6, 0, 0), "QT >= 6.0.0 required");

int main(int argc, char *argv[])
{
    QGuiApplication app(argc, argv);
    QQmlApplicationEngine engine;
    const QUrl url(QStringLiteral("qrc:/view/main.qml"));
    QObject::connect(&engine, &QQmlApplicationEngine::objectCreated,
                     &app, [url](QObject *obj, const QUrl &objUrl) {
        if (!obj && url == objUrl)
            QCoreApplication::exit(-1);
    }, Qt::QueuedConnection);

    app.setWindowIcon(QIcon(APP_ICON_PATH));
    qmlRegisterSingletonType(QString("file:///").append(COLORS_PATH), "com.discode.colors", 1, 0, "Colors");
    qmlRegisterSingletonType(QString("file:///").append(FONT_PATH), "com.discode.fonts", 1, 0, "Font");

    auto api_conf{std::make_shared<api_config>(IPS_PATH, ROUTES_PATH)};

    auto bm{std::make_shared<ban_model>()};
    engine.rootContext()->setContextProperty("banModel", bm.get());
    auto rm{std::make_shared<report_model>()};
    engine.rootContext()->setContextProperty("reportModel", rm.get());

    auto as{std::make_shared<authentication::authentication_service>(api_conf)};
    auto bs{std::make_shared<ban_space::ban_service>(api_conf)};
    auto ss{std::make_shared<session_service>()};

    authentication_controller ac{as, ss};
    engine.rootContext()->setContextProperty("authenticationController", &ac);
    ban_controller bc{bs, ss, bm};
    engine.rootContext()->setContextProperty("banController", &bc);
    report_controller rc{};
    engine.rootContext()->setContextProperty("reportController", &rc);

    engine.load(url);

    return app.exec();
}
