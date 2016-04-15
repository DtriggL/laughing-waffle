<?php

use Phalcon\Mvc\Model;

class Crumb extends Model
{
    public $crumb_id;

    public $longitude;

    public $latitude;
    
    public $message;

    public $title;
    
    public $rating;
    
    public $ratings;
    
    public $creation_date;
    
    public $total_discovered;
    
    public $creator_id;

    public function getSource()
    {
        return "Crumb";
    }
    public function initialize()
    {
        $this->setSource("Crumb");
    }
}
