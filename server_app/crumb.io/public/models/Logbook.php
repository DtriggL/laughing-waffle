<?php

use Phalcon\Mvc\Model;

class Logbook extends Model
{
    public $entry_id;

    public $user_id;
    
    public $content;

    public function initialize()
    {
        $this->setSource("Logbook");
    }
}
