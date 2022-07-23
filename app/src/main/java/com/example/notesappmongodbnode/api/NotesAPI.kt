package com.example.notesappmongodbnode.api

import com.example.notesappmongodbnode.model.NoteRequest
import com.example.notesappmongodbnode.model.NoteResponse
import com.example.notesappmongodbnode.utils.Constants.NOTE
import com.example.notesappmongodbnode.utils.Constants.NOTE_ID
import retrofit2.Response
import retrofit2.http.*

interface NotesAPI {

    @GET(NOTE)
    suspend fun getNotes(): Response<List<NoteResponse>>

    @POST(NOTE)
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    @PUT(NOTE_ID)
    suspend fun updateNote(@Path("noteId") noteId:String, @Body noteRequest: NoteRequest): Response<NoteResponse>

    @DELETE(NOTE_ID)
    suspend fun deleteNote(@Path("noteId") noteId: String) : Response<NoteResponse>
}