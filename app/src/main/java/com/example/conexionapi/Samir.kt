package com.example.conexionapi

import com.google.gson.annotations.SerializedName

data class SamirResponse(
    @SerializedName("titulo"            ) var titulo            : String? = null,
    @SerializedName("precio"            ) var precio            : Float?    = null,
    @SerializedName("descripcion"       ) var descripcion       : String? = null,
    @SerializedName("metrosConstruidos" ) var metrosConstruidos : Int?    = null,
    @SerializedName("metrosUtiles"      ) var metrosUtiles      : Int?    = null,
    @SerializedName("ubicacion"         ) var ubicacion         : String? = null,
    @SerializedName("zona"              ) var zona              : String? = null,
    @SerializedName("fechaPublicacion"  ) var fechaPublicacion  : String? = null,
    @SerializedName("habitaciones"      ) var habitaciones      : Int?    = null,
    @SerializedName("bannos"            ) var bannos            : Int?    = null,
    @SerializedName("idInmueble"        ) var idInmueble        : Int?    = null
)