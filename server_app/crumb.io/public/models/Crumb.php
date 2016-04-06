<?php

use Phalcon\Mvc\Model;

class Crumb extends Model
{
    public $note_id;

    public $user_id;

    public $longitude;

    public $latitude;

    public $text;

    public $title;

    public function getSource()
    {
        return "Crumb";
    }
    public function initialize()
    {
        $this->setSource("Crumb");
    }
}
