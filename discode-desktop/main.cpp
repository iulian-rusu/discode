#include <authentication_controller.h>

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

    authentication_controller ac{};

    engine.rootContext()->setContextProperty("authenticationController", &ac);

    engine.load(url);

    return app.exec();
}
