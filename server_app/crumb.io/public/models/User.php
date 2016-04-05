<?php

use Phalcon\Mvc\Model;

class User extends Model
{
    public $user_id;

    public $user_name;

    public $first_name;

    public $last_name;

    public $email;

    public $password;

    public function initialize()
    {
        $this->setSource("User");
    }
}
