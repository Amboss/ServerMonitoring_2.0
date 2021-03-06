<#-- ==============================================================
     HEADER part of common layout
     ============================================================== -->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
     "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
    <!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
    <!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
    <!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
    <!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <link rel="shortcut icon" href="<@spring.url '/static/img/favicon_02.ico'/>" type="image/x-icon"/>
        <link rel="stylesheet" type="text/css" href="<@spring.url '/static/css/bootstrap.css'/>"/>
        <link rel="stylesheet" type="text/css" href="<@spring.url '/static/css/DT_bootstrap.css'/>"/>

        <style type="text/css">
            body {
                padding-top: 60px;
                padding-bottom: 40px;
            }
            .hero-unit {
                background-image: url("<@spring.url '/static/img/header_default2.png'/>");
                background-repeat: no-repeat;
            }
            table.table thead tr th {
                text-align: center;
            }
            table.table tbody tr td {
                text-align: center;
            }
        </style>

        <script type="text/javascript" charset="utf-8" src="http://code.jquery.com/jquery-latest.js" ></script>
        <script type="text/javascript" charset="utf-8" src="<@spring.url '/static/js/jquery.validate.js'/>" ></script>
        <script type="text/javascript" charset="utf-8" src="<@spring.url '/static/js/jquery.i18n.properties.js'/>" ></script>
        <script type="text/javascript" charset="utf-8" src="<@spring.url '/static/js/dataTables.js'/>" ></script>
        <script type="text/javascript" charset="utf-8" src="<@spring.url '/static/js/DT_bootstrap.js'/>" ></script>
        <script type="text/javascript" charset="utf-8" src="<@spring.url '/static/js/bootstrap.js'/>" ></script>

        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"; charset="utf-8">
        <meta name="description" content="Server Monitoring Service">
        <title>${title?html}</title>
    </head>
    <body>
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an outdated browser. <a href="http://browsehappy.com/">
            Upgrade your browser today</a> or <a href="http://www.google.com/chromeframe/?redirect=true">
            install Google Chrome Frame</a> to better experience this site.</p>
        <![endif]-->
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="brand" href="<@spring.url '/employee/monitoring.html' />"><@spring.message "header.title" /></a>
                    <div class="nav-collapse collapse">
                        <div class="header_menu">
                            <#include "header/header_menu.ftl">
                        </div>
                        <div class="nav-collapse collapse">
                            <#include "header/login_form.ftl"/>
                        </div>
                    </div>
                </div><!-- /.container -->
            </div><!-- /.navbar-inner -->
        </div><!-- /.navbar navbar-inverse -->

