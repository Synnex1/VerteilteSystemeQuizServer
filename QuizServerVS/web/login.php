<?php
require_once __DIR__ . '/vendor/autoload.php';

session_start();

$fb = new Facebook\Facebook([
  'app_id' => 'app id', // Replace {app-id} with your app id
  'app_secret' => 'app secret',
  'default_graph_version' => 'v2.2',
  ]);

$helper = $fb->getRedirectLoginHelper();

$permissions = ['email']; // Optional permissions
$loginUrl = $helper->getLoginUrl('localhost:8080/fb-callback.php', $permissions);

echo '<a href="' . htmlspecialchars($loginUrl) . '">Log in with Facebook!</a>';
