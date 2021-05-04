package no.uia.ikt205.piano.data


data class Note(val value:String, val start: String, val end: String){
    override fun toString(): String{
        return "Note: $value,Seconds from start: $start,Seconds played: $end"
    }
}


