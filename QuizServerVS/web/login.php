<?php
require_once __DIR__ . '/vendor/autoload.php';

session_start();

$fb = new Facebook\Facebook([
  'app_id' => '1646952125627037', // Replace {app-id} with your app id
  'app_secret' => 'e7dd3ce3199939390d9e31f2c81e52dc',
  'default_graph_version' => 'v2.2',
  ]);

$helper = $fb->getRedirectLoginHelper();

$permissions = ['email']; // Optional permissions
$loginUrl = $helper->getLoginUrl('localhost:8080/fb-callback.php', $permissions);

echo '<a href="' . htmlspecialchars($loginUrl) . '">Log in with Facebook!</a>';
