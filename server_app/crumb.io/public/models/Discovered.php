<?php

use Phalcon\Mvc\Model;

class Discovered extends Model
{
    public $u_id;

    public $c_id;

    public function initialize()
    {
        $this->setSource("Discovered");
    }
}
